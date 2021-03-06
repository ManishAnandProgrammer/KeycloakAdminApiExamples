package examples;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class KeycloakAdmin_UsingToken {

  // this token must contain realm-management roles
  private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIwYVB0a1Y3dGp4azh1WTR1SmdGRWtQYTU4N2dXZjhHeHZZNi1nOExpZXhRIn0.eyJleHAiOjE2MTcyNjEwODgsImlhdCI6MTYxNzI1NzQ4OCwianRpIjoiNDUxNGUzMzktZjRmZi00N2VlLThkZjktMWVmNGFmNDcxYTY4IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgwL2F1dGgvcmVhbG1zL3NwcmluZ2Jvb3RkZW1vIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6IjdlZTBkYzg1LWU2OGQtNGM3Zi1hNjU2LWFlYzAxM2ZjMGUxOSIsInR5cCI6IkJlYXJlciIsImF6cCI6Im15QXBwIiwic2Vzc2lvbl9zdGF0ZSI6ImE2ZGIwNjc1LWRiNTMtNDU5My05Yzg2LTdhMDY0OWUwMzk1MSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsidmlldy1yZWFsbSIsInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJyZWFsbS1hZG1pbiIsImNyZWF0ZS1jbGllbnQiLCJtYW5hZ2UtdXNlcnMiLCJxdWVyeS1yZWFsbXMiLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzIiwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHMiLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfSwiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJuZWVyYWoifQ.DNMa-kALN1S1rBa5Uswlb0ItAzwI6Xs0PMenWHSG0ZqrQOUUysQMUUIBp_-SI57NMQmG1f_KkfiGTKcpq3a9uE-eOVI9-AI2Mv9no29ntmQTgO0WTYInG839bJV13wp1FVZ_wySs5DOR2cUDKD7DiVUJPPdueiakLbV7cNmaHq25yVX1G5G4nRZEmshXA3bV0dbR9VN_9lpEbpCox1pyPQUqyA9x__SzgwjUtK0LbapAHWxy1-FJt4O3mqbB-shZQqzlfdyzLkIkLJpweLUtnu-4Vl4H1humMZMhWkA0GmtwGBk57BSZ7DCK99UzBelnMR6MX0kMRDiogOebO_mKVw";
  private static final String KEYCLOAK_SERVER_URL = "http://localhost:8080/auth";
  private static final String REALM_NAME = "springboard";

  // Don't get confuse in client id and client's id
  // client id is like client name and client's id
  // looks like '849a4d2f-2dd7-4d7b-a590-4f677cf4b674'
  private static final String CLIENT_ID = "myApp";

  private final static Keycloak KEYCLOAK = Keycloak.getInstance(KEYCLOAK_SERVER_URL,
          REALM_NAME, CLIENT_ID, TOKEN);

  public static void main(String[] args) {
    createUser();
    printAllRealmRoles();
    listAllUser();
    findUser();
    findUserAndSetRole();
    printAllClients();
    clientRoles();
  }

  public static void clientRoles() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    ClientsResource clientsResource = realmResource.clients();
    // we are providing client's id here not client name
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
    list.forEach(KeycloakAdmin_UsingToken::printUserRepresentation);

  }

  public static void findUserAndSetRole() {
    RealmResource realmResource = KEYCLOAK.realm(REALM_NAME);

    UsersResource usersResource = realmResource.users();
    List<UserRepresentation> list = usersResource.search("9817330011", true);
    Optional<UserRepresentation> userRepresentationOptional = list.stream().findFirst();

    if(userRepresentationOptional.isPresent()) {
      UserRepresentation userRepresentation = userRepresentationOptional.get();
      RolesResource rolesResource = realmResource.roles();

      // if the role not found it will throw exception
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
    userRepresentationList.forEach(KeycloakAdmin_UsingToken::printUserRepresentation);

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
    UsersResource userResources = realmResource.users();

    //Create user (requires manage-users role)
    Response response = userResources.create(user);

    String userId = response.getLocation()
      .getPath().replaceAll(".*/([^/]+)$", "$1");

    System.out.printf("User created with userId: %s%n", userId);

    // requires view-realm role
    RolesResource rolesResource = realmResource.roles();

    RoleRepresentation roleRepresentation =
      realmResource.roles()//
        .get("user").toRepresentation();

    // Assign realm role tester to user
    userResources.get(userId).roles().realmLevel() //
      .add(Arrays.asList(roleRepresentation));

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
