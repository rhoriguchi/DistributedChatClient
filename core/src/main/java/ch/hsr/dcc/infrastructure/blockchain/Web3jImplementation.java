package ch.hsr.dcc.infrastructure.blockchain;

import ch.hsr.dcc.Constants;
import org.slf4j.LoggerFactory;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class Web3jImplementation implements BlockChainGateway {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Web3jImplementation.class);

    private final Web3j web3j;

    public Web3jImplementation(Web3j web3j) {
        this.web3j = web3j;
    }

    @Override
    public synchronized void executeNotarizeHashTransaction(Credentials credentials, byte[] hash) {
        try {
            String notarizeHashTransaction = createNotarizeHashTransaction(credentials, hash);
            EthSendTransaction sendTransaction = web3j.ethSendRawTransaction(notarizeHashTransaction).send();

            if (sendTransaction.hasError()) {
                //TODO wrong exception type
                throw new IllegalArgumentException(sendTransaction.getError().getMessage());
            }

            EthGetTransactionReceipt transactionReceipt = null;
            while (transactionReceipt == null || transactionReceipt.getResult() == null) {
                transactionReceipt = web3j.ethGetTransactionReceipt(sendTransaction.getTransactionHash()).send();

                //TODO find better solution
                // https://web3j.readthedocs.io/en/latest/smart_contracts.html
                // https://github.com/web3j/web3j/blob/master/core/src/main/java/org/web3j/tx/response/PollingTransactionReceiptProcessor.java
                try {
                    wait(60_000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }

            if (!transactionReceipt.getResult().isStatusOK()) {
                //TODO wrong exception type
                throw new IllegalArgumentException("Could not execute transaction to save hash on blockchain");
            }
        } catch (IOException e) {
            // TODO do something with exception
            LOGGER.error(e.getMessage(), e);
            //TODO wrong exception type
            throw new IllegalArgumentException("Could not execute transaction to save hash on blockchain");
        }
    }

    private String createNotarizeHashTransaction(Credentials credentials,
                                                 byte[] hash) {
        try {
            BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(),
                DefaultBlockParameterName.LATEST
            ).send().getTransactionCount();

            String encodedFunction = FunctionEncoder.encode(
                new Function(
                    Constants.SMART_CONTRACT_NOTARIZE,
                    Collections.singletonList(new Bytes32(hash)),
                    new ArrayList<>()
                )
            );

            RawTransaction transaction = RawTransaction.createTransaction(
                nonce,
                getGasPrice(),
                getGasLimit(),
                Constants.SMART_CONTRACT_ADDRESS,
                BigInteger.ZERO,
                encodedFunction
            );

            byte[] signedMessage = TransactionEncoder.signMessage(transaction, credentials);

            return Numeric.toHexString(signedMessage);
        } catch (IOException e) {
            // TODO do something with exception
            LOGGER.error(e.getMessage(), e);
            //TODO wrong exception type
            throw new IllegalArgumentException("Could not create transaction to save hash on blockchain");
        }
    }

    //TODO mock get this from some source
    // https://rinkeby.etherscan.io/blocks -> column GasUsed
    private BigInteger getGasPrice() {
        return BigInteger.valueOf(2_000_000);
    }

    //TODO mock get this from some source
    // https://rinkeby.etherscan.io/blocks -> column GasLimit
    private BigInteger getGasLimit() {
        return BigInteger.valueOf(7_000_000);
    }

    @Override
    public synchronized boolean verifyNotarizedHash(Credentials credentials, byte[] hash) {
        try {
            Function function = new Function(
                Constants.SMART_CONTRACT_VERIFY,
                Arrays.asList(new Address(credentials.getAddress()), new Bytes32(hash)),
                Collections.singletonList(TypeReference.create(Bool.class))
            );

            Transaction transaction = Transaction.createEthCallTransaction(
                credentials.getAddress(),
                Constants.SMART_CONTRACT_ADDRESS,
                FunctionEncoder.encode(function)
            );

            EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST)
                .send();

            if (ethCall.hasError()) {
                //TODO wrong exception type
                throw new IllegalArgumentException("Could not verify transaction to check hash on blockchain");
            }

            //TODO ugly
            return (boolean) FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters())
                .get(0)
                .getValue();
        } catch (IOException e) {
            // TODO do something with exception
            LOGGER.error(e.getMessage(), e);
            //TODO wrong exception type
            throw new IllegalArgumentException("Could not verify transaction to check hash on blockchain");
        }
    }
}
