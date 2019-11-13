package org.health.controller;

import org.health.entity.Role;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RoleControllerIntegrationTest {
    private static final String ROOT = "http://localhost:8080/role/";
    private static final String ADD = "add";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String GET = "get";
    private static final String ALL = "all";

    @Test
    public void testDelAddGet() {
        Role role = createRole();

        RestTemplate template = new RestTemplate();
        ResponseEntity<Role> responseEntity = template.exchange(
                ROOT + GET + "/{id}",
                HttpMethod.GET,
                null,
                Role.class,
                role.getId()
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Role receivedRole = responseEntity.getBody();
        assertNotNull(receivedRole);

        getAllRoles();

        deleteRolebyId(role.getId());
    }

    private Role createRole() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        Role role = prefillRole();
        HttpEntity<Role> entity = new HttpEntity<>(role, headers);
        RestTemplate template = new RestTemplate();
        Role createdRole = template.exchange(
                ROOT + ADD,
                HttpMethod.POST,
                entity,
                Role.class
        ).getBody();

        assertNotNull(createdRole);
        assertEquals("ROLE_TEST", createdRole.getName());

        return createdRole;
    }

    private Role prefillRole() {
        Role role = new Role();
        role.setName("ROLE_TEST");
        role.setDescription("test role");
        return role;
    }

    private List<Role> getAllRoles() {
        RestTemplate template = new RestTemplate();
        ResponseEntity<Role[]> response =
                template.getForEntity(
                        "http://localhost:8080/role/all/",
                        Role[].class);

        List<Role> roleList = Arrays.asList(response.getBody());
        assertNotNull(roleList);
        return roleList;
    }

    private void deleteRolebyId(long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        Role role = prefillRole();
        HttpEntity<Role> entity = new HttpEntity<>(role, headers);
        RestTemplate template = new RestTemplate();
        Role roleDel = template.exchange(
                ROOT + DELETE + "/" + id,
                HttpMethod.DELETE,
                entity,
                Role.class
        ).getBody();

        assertNotNull(roleDel);
    }
}
