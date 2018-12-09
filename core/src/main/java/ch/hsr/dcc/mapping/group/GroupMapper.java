package ch.hsr.dcc.mapping.group;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.group.GroupChangedTimeStamp;
import ch.hsr.dcc.domain.group.GroupName;
import ch.hsr.dcc.domain.keystore.Sign;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.db.DbGroup;
import ch.hsr.dcc.infrastructure.db.DbIdGenerator;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupAdd;
import ch.hsr.dcc.mapping.Util.TomP2PPeerAddressHelper;
import ch.hsr.dcc.mapping.keystore.KeyStoreRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupMapper implements GroupRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(GroupMapper.class);

    private final DbGateway dbGateway;
    private final TomP2P tomP2P;

    private final PeerRepository peerRepository;
    private final KeyStoreRepository keyStoreRepository;

    public GroupMapper(TomP2P tomP2P,
                       DbGateway dbGateway,
                       PeerRepository peerRepository,
                       KeyStoreRepository keyStoreRepository) {
        this.tomP2P = tomP2P;
        this.dbGateway = dbGateway;
        this.peerRepository = peerRepository;
        this.keyStoreRepository = keyStoreRepository;
    }

    @Override
    public void save(Group group) {
        try {
            TomP2PGroupObject tomP2PGroupObject = groupToTomP2PGroupObject(group);

            tomP2P.addGroupObject(tomP2PGroupObject);
            dbGateway.saveGroup(tomP2PGroupObjectToDbGroup(tomP2PGroupObject));
            //TODO to broad exception
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            //TODO wrong exception type
            throw new IllegalArgumentException("Failed to save group");
        }
    }

    private TomP2PGroupObject groupToTomP2PGroupObject(Group group) {
        long id;
        if (group.getId().isEmpty()) {
            id = group.getId().toLong();
        } else {
            id = generateGroupIdAndCheckIfUsed();
        }

        //TODO use new function
        TomP2PGroupObject tomP2PGroupObject = new TomP2PGroupObject(
            id,
            group.getName().toString(),
            group.getAdmin().getUsername().toString(),
            group.getMembers().stream()
                .map(Peer::getUsername)
                .map(Username::toString)
                .collect(Collectors.toSet()),
            group.getLastChanged().toString(),
            null
        );

        tomP2PGroupObject.setSignature(keyStoreRepository.sign(tomP2PGroupObject).toString());

        return tomP2PGroupObject;
    }

    private Long generateGroupIdAndCheckIfUsed() {
        Long id;
        Optional<TomP2PGroupObject> groupObject;

        do {
            id = DbIdGenerator.getId();
            groupObject = tomP2P.getGroupObject(id);
        } while (groupObject.isPresent());

        return id;
    }

    private DbGroup tomP2PGroupObjectToDbGroup(TomP2PGroupObject tomP2PGroupObject) {
        return new DbGroup(
            tomP2PGroupObject.getId(),
            tomP2PGroupObject.getName(),
            tomP2PGroupObject.getAdminUsername(),
            tomP2PGroupObject.getMembers(),
            tomP2PGroupObject.getLastChanged(),
            tomP2PGroupObject.getSignature()
        );
    }

    @Override
    public Optional<Group> get(GroupId groupId) {
        Optional<DbGroup> optionalDbGroup = dbGateway.getGroup(groupId.toLong());

        if (optionalDbGroup.isPresent()) {
            return optionalDbGroup.map(this::dbGroupToGroup);
        } else {
            return tomP2P.getGroupObject(groupId.toLong())
                .map(tomP2PGroupObject -> {
                    DbGroup dbGroup = dbGateway.saveGroup(tomP2PGroupObjectToDbGroup(tomP2PGroupObject));
                    return Optional.of(dbGroupToGroup(dbGroup));
                }).orElseGet(Optional::empty);
        }
    }

    private Group dbGroupToGroup(DbGroup dbGroup) {
        return new Group(
            GroupId.fromLong(dbGroup.getId()),
            GroupName.fromString(dbGroup.getName()),
            peerRepository.get(Username.fromString(dbGroup.getAdmin())),
            dbGroup.getMembers().stream()
                .map(Username::fromString)
                .map(peerRepository::get)
                .collect(Collectors.toSet()),
            GroupChangedTimeStamp.fromString(dbGroup.getLastChanged()),
            Sign.fromString(dbGroup.getSignature())
        );
    }

    @Override
    public Stream<Group> getAll(Username username) {
        return dbGateway.getAllGroups(username.toString())
            .map(this::dbGroupToGroup);
    }

    @Override
    //TODO delete group out of local db if not part of it
    public void synchronizeGroups() {
        LOGGER.debug("Starting group synchronization...");

        Peer self = peerRepository.getSelf();
        dbGateway.getAllGroups(self.getUsername().toString())
            .forEach(dbGroup -> {
                Optional<TomP2PGroupObject> optionalTomP2PGroupObject = tomP2P.getGroupObject(dbGroup.getId());

                if (optionalTomP2PGroupObject.isPresent()) {
                    TomP2PGroupObject tomP2PGroupObject = optionalTomP2PGroupObject.get();

                    GroupChangedTimeStamp dbTimeStamp = GroupChangedTimeStamp.fromString(dbGroup.getLastChanged());
                    GroupChangedTimeStamp tomP2PTimeStamp = GroupChangedTimeStamp.fromString(tomP2PGroupObject
                        .getLastChanged());

                    if (dbTimeStamp.equals(tomP2PTimeStamp)) {
                        if (dbTimeStamp.isAfter(tomP2PTimeStamp)) {
                            tomP2P.addGroupObject(dbGroupToTomP2PGroupObject(dbGroup));
                        } else {
                            dbGateway.saveGroup(tomP2PGroupObjectToDbGroup(tomP2PGroupObject));
                        }
                    }
                } else {
                    tomP2P.addGroupObject(dbGroupToTomP2PGroupObject(dbGroup));
                }
            });

        LOGGER.debug("Done group synchronization");
    }

    private TomP2PGroupObject dbGroupToTomP2PGroupObject(DbGroup dbGroup) {
        return new TomP2PGroupObject(
            dbGroup.getId(),
            dbGroup.getName(),
            dbGroup.getAdmin(),
            dbGroup.getMembers(),
            dbGroup.getLastChanged(),
            dbGroup.getSignature()
        );
    }

    @Override
    public void sendGroupAdd(Group group, Peer peer) {
        try {
            tomP2P.sendGroupAdd(groupToTomP2PGroupAdd(group),
                TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            //TODO wrong exception
            throw new IllegalArgumentException(String.format("Can't add %s to group %s",
                peer.getUsername(),
                group.getName()));
        }
    }

    private TomP2PGroupAdd groupToTomP2PGroupAdd(Group group) {
        return new TomP2PGroupAdd(
            group.getId().toLong(),
            group.getName().toString(),
            group.getAdmin().getUsername().toString(),
            group.getLastChanged().toString(),
            group.getMembers().stream()
                .map(Peer::getUsername)
                .map(Username::toString)
                .collect(Collectors.toSet()),
            group.getSign().toString()
        );
    }

    @Override
    public Group getOldestGroupAdd() {
        return TomP2PGroupAddToGroup(tomP2P.getOldestReceivedTomP2PGroupAdd());
    }

    private Group TomP2PGroupAddToGroup(TomP2PGroupAdd tomP2PGroupAdd) {
        return new Group(
            GroupId.fromLong(tomP2PGroupAdd.getId()),
            GroupName.fromString(tomP2PGroupAdd.getName()),
            peerRepository.get(Username.fromString(tomP2PGroupAdd.getAdminUsername())),
            tomP2PGroupAdd.getMembers().stream()
                .map(Username::fromString)
                .map(peerRepository::get)
                .collect(Collectors.toSet()),
            GroupChangedTimeStamp.fromString(tomP2PGroupAdd.getLastChanged()),
            Sign.fromString(tomP2PGroupAdd.getSignature())
        );
    }

    @Override
    public void addGroup(Group group) {
        dbGateway.saveGroup(groupToDbGroup(group));
    }

    private DbGroup groupToDbGroup(Group group) {
        return new DbGroup(
            group.getId().toLong(),
            group.getName().toString(),
            group.getAdmin().getUsername().toString(),
            group.getMembers().stream()
                .map(Peer::getUsername)
                .map(Username::toString)
                .collect(Collectors.toSet()),
            group.getLastChanged().toString(),
            group.getSign().toString()
        );
    }
}
