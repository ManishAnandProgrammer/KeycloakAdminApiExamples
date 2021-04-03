# KeycloakAdminApiExamples
    Keycloak Provides Admin Api in java to interact with keycloak
    and do whatever operation you can do as a admin from Keycloak UI dashboard.

# note
     The User needs at least "manage-users, view-clients,
     view-realm, view-users" roles for "realm-management" to use AdminApi.
     
     To Assign Above Roles To User:
        Go to the User's Account, under role mapping section go to client 
        roles and click on realm-management, select all if you want to 
        give the full access to user otherwise choose according to need.
        
# Keyclaok Admin Api Using Password Flow :
    In this flow user must have above role mentioned, and user have to provide 
    his username and password. Not a Recommanded Way.
  
    private final static Keycloak KEYCLOAK = KeycloakBuilder.builder()
      .serverUrl("http://localhost:8080/auth")
      .realm("springbootdemo")
      .grantType(OAuth2Constants.PASSWORD)
      .clientId("myApp")
      .clientSecret("02a8a752-107c-406e-a0f6-169650799c50")
      .username("manish")
      .password("anand")
      .build();
    
    Change url, realm, clientId, clientSecret, username and password according 
    your configurations.
    
  # Keycloak Admin Api Using Token : 
      In this flow User's token must contain the valid roles to do operation.
      This is the recommanded way of using Admin Api.

       private final static Keycloak keycloak = 
          Keycloak.getInstance("http://localhost:8080/auth",
                              "springbootdemo", "myApp",
                                "your Token Here");

    
