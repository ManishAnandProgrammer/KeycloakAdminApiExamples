package examples;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class KeycloakAdmin_UsingPassword {

  private static final String KEYCLOAK_SERVER_URL = "http://localhost:8080/auth";
  private static final String REALM_NAME = "springboard";

  // Don't get confuse in client id and client's id
  // client id is like client name and client's id
  // looks like '849a4d2f-2dd7-4d7b-a590-4f677cf4b674'
  private static final String CLIENT_ID = "myApp";

  private final static Keycloak KEYCLOAK = KeycloakBuilder.builder() //
    .serverUrl(KEYCLOAK_SERVER_URL) //
    .realm(REALM_NAME) //
    .grantType(OAuth2Constants.PASSWORD) //
    .clientId(CLIENT_ID)
    .clientSecret("02a8a752-107c-406e-a0f6-169650799c50") //
    .username("neeraj") //
    .password("jangra") //
    .build();

  public static void main(String[] args) {
    createUser();
    printAllRealmRoles();
    listAllUser();
    findUser();
    findUserAndSetRole();
    printAllClients();
    clientRoles();
  }

  // User "neeraj" needs at least "manage-users, view-clients,
  // view-realm, view-users" roles for "realm-management"
  public static void clientRoles() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    ClientsResource clientsResource = realmResource.clients();
    ClientResource clientResource = clientsResource.get("849a4d2f-2dd7-4d7b-a590-4f677cf4b674");
    if(clientResource != null) {
      RolesResource rolesResource = clientResource.roles();
      rolesResource.list().forEach(roleRepresentation -> System.out.println(roleRepresentation.toString()));
    } else {
      System.out.println("No myApp Client found");
    }

  }

  public static void printAllClients() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    ClientsResource clientsResource = realmResource.clients();
    List<ClientRepresentation> clientRepresentationList = clientsResource.findAll();

    for(ClientRepresentation clientRepresentation : clientRepresentationList) {
      System.out.println(clientRepresentation.getId());
      System.out.println(clientRepresentation.getName());
    }
  }

  public static void findUser() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    UsersResource usersResource = realmResource.users();
    List<UserRepresentation> list = usersResource.search("9817330020", true);
    list.forEach(KeycloakAdmin_UsingPassword::printUserRepresentation);

  }

  public static void findUserAndSetRole() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    UsersResource usersResource = realmResource.users();
    List<UserRepresentation> list = usersResource.search("9817330011", true);
    Optional<UserRepresentation> userRepresentationOptional = list.stream().findFirst();

    if(userRepresentationOptional.isPresent()) {
      UserRepresentation userRepresentation = userRepresentationOptional.get();
      RolesResource rolesResource = realmResource.roles();

      // if role is not present in keycloak it will throw exception
      RoleResource roleResource = rolesResource.get("admin");
      RoleRepresentation roleRepresentation = roleResource.toRepresentation();

      usersResource.get(userRepresentation.getId())
                    .roles().realmLevel().add(List.of(roleRepresentation));

    } else {
      System.out.println("User Not found With Given UserName");
    }

  }

  public static void printAllRealmRoles() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    RolesResource rolesResource = realmResource.roles();
    List<RoleRepresentation> roleRepresentationList = rolesResource.list();

    roleRepresentationList.forEach(roleRepresentation ->
      System.out.println(roleRepresentation.toString()));
  }

  public static void listAllUser() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    UsersResource userResource = realmResource.users();
    List<UserRepresentation> userRepresentationList = userResource.list();
    userRepresentationList.forEach(KeycloakAdmin_UsingPassword::printUserRepresentation);

  }

  public static void printUserRepresentation(UserRepresentation userRepresentation) {
    System.out.println("User ::");
    System.out.println(userRepresentation.getId());
    System.out.println(userRepresentation.getUsername());
    System.out.println();
  }

  public static void createUser() {
    UserRepresentation user = getUserRepresentation();

    // Get realm
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);
    UsersResource userRessource = realmResource.users();

    //Create user (requires manage-users role)
    Response response = userRessource.create(user);

    System.out.println("Repsonse: " + response.getStatusInfo());
    System.out.println(response.getLocation());

    String userId = response.getLocation()
      .getPath().replaceAll(".*/([^/]+)$", "$1");

    System.out.printf("User created with userId: %s%n", userId);

    RoleRepresentation userRealmRole =
      realmResource.roles()//
        .get("user").toRepresentation();

    userRessource.get(userId).roles().realmLevel() //
      .add(Arrays.asList(userRealmRole));

  }

  private static UserRepresentation getUserRepresentation() {
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername("myUserName");
    user.setFirstName("myFirstName");
    user.setLastName("myLastName");
    user.setEmail("myEmail@gmail.com");
    user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));
    return user;
  }

}
