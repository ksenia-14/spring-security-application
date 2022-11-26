package com.example.springsecurityapplication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringSecurityApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() throws Exception {
//        this.mockMvc.perform(get("/index")) // отправляем запрос на /index
//                .andDo(print())     // результат вывести в print()
//                .andExpect(status().is3xxRedirection()) // в качестве статуса придет статус 300
//                .andExpect(redirectedUrl("http://localhost:8081/authentication/login")); // при запросе на статус 300 перебросит на url
    }

}
