
import ExtractForJSON.FirstTest.Root;
import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ApiTests {
    private static int idPet;

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }


    @Test
    @Step
    @DisplayName("Тест №1: Добавление питомца")
    public void t1_addNewPet() {
        File json = new File("C:\\Users\\jense\\Git\\RestAssured2\\src\\main\\resources\\test1\\newPet.json");
        try {
            Response response;
            response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .when()
                    .post("/pet");
            response.then().assertThat().statusCode(200);

            // Помещаю переданный ID в отдельную переменную, отталкиваясь от ответа
            idPet = response.body().as(Root.class).getId();
//            System.out.println(idPet); // Логирую, что нужный ID помещен
            if (response.getStatusCode() == 200) {
                System.out.println("Тест №1 прошел успешно");
            } else {
                System.out.println("Тест №1 неудачен. Код состояния: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.out.println("Тест №1 завершился с ошибокой: " + e.getMessage());
            e.printStackTrace();

        }


    }

    @Test
    @Step
    @DisplayName("Тест №2: Проверка того, что питомец был создан")
    public void t2_checkNewPet() {
        try {
            Response response;
            response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .get("/pet/" + idPet);
            response.then().assertThat().statusCode(200);
            if (response.getStatusCode() == 200) {
                System.out.println("Тест №2 прошел успешно");
            } else if (response.getStatusCode() != 200) {
                System.out.println(response.getStatusCode());
            }

        } catch (Exception e) {
            System.out.println("Тест №2 завершился с ошибокой: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Test
    @Step
    @DisplayName("Тест №3: Обновить инфу по питомцу")
    public void t3_editPet() {
        File json = new File("C:\\Users\\jense\\Git\\RestAssured2\\src\\main\\resources\\test2\\editPet.json");
        try {
            Response response;
            response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(json)
                    .when()
                    .put("/pet");
            response.then().assertThat().statusCode(200);
            if (response.getStatusCode() == 200) {
                System.out.println("Тест №3 прошел успешно");
            } else if (response.getStatusCode() != 200) {
                System.out.println(response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Тест №3 завершился с ошибокой: " + e.getMessage());
            e.printStackTrace();
        }

    }


    @Test
    @Step
    @DisplayName("Тест №4: Проверка того, что инфа у питомца была обновлена")
    public void t4_checkUpdatePet() {
        try {
            Response response;
            response = given()
                    .header("Content-type", "application/json")
                    .and()
                    .get("/pet/" + idPet);
            response.then().assertThat().statusCode(200);

            Root root = response.body().as(Root.class);
            assertNotNull(root);

            Gson gson = new Gson();
            String json = gson.toJson("Новое имя: " + root.getName() + " Новый тег: " + root.getTags().get(0).getName()+ " Новый статус:" + root.getStatus());
            System.out.println("Измененные данные: " + json);


            if (response.getStatusCode() == 200) {
                System.out.println("Тест №4 прошел успешно");
            } else if (response.getStatusCode() != 200) {
                System.out.println(response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Тест №4 завершился с ошибокой: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
