import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import io.vertx.sqlclient.templates.SqlTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
class ContextTest {

    @BeforeEach
    void before(Vertx vertx, VertxTestContext testContext) {
        vertx.getOrCreateContext().putLocal("key", "test");
        final var pool = JDBCPool.pool(vertx, new JsonObject().put("url", "jdbc:sqlite:" + "/tmp/test.db"));
        pool.withTransaction(tx -> {
            final String sql = """
                    CREATE TABLE IF NOT EXISTS test
                    (
                        id            INTEGER PRIMARY KEY AUTOINCREMENT,
                        name          TEXT    NOT NULL
                    )
                    """;
            return SqlTemplate.forUpdate(tx, sql).execute(Collections.emptyMap());
        }).onComplete(testContext.succeedingThenComplete());
    }

    @AfterEach
    void after(Vertx vertx, VertxTestContext testContext) {
        vertx.fileSystem().delete("/tmp/test.db").onComplete(testContext.succeedingThenComplete());
    }


    //    @RepeatedTest(10)
    @Test
    void testContext(Vertx vertx, VertxTestContext testContext) {
        final String data ="""
                {
                    "data": [
                    {
                        "name": "Adeel Solangi",
                        "language": "Sindhi",
                        "id": "V59OF92YF627HFY0",
                        "bio": "Donec lobortis eleifend condimentum. Cras dictum dolor lacinia lectus vehicula rutrum. Maecenas quis nisi nunc. Nam tristique feugiat est vitae mollis. Maecenas quis nisi nunc.",
                        "version": 6.1
                      },
                      {
                        "name": "Afzal Ghaffar",
                        "language": "Sindhi",
                        "id": "ENTOCR13RSCLZ6KU",
                        "bio": "Aliquam sollicitudin ante ligula, eget malesuada nibh efficitur et. Pellentesque massa sem, scelerisque sit amet odio id, cursus tempor urna. Etiam congue dignissim volutpat. Vestibulum pharetra libero et velit gravida euismod.",
                        "version": 1.88
                      },
                      {
                        "name": "Aamir Solangi",
                        "language": "Sindhi",
                        "id": "IAKPO3R4761JDRVG",
                        "bio": "Vestibulum pharetra libero et velit gravida euismod. Quisque mauris ligula, efficitur porttitor sodales ac, lacinia non ex. Fusce eu ultrices elit, vel posuere neque.",
                        "version": 7.27
                      },
                      {
                        "name": "Abla Dilmurat",
                        "language": "Uyghur",
                        "id": "5ZVOEPMJUI4MB4EN",
                        "bio": "Donec lobortis eleifend condimentum. Morbi ac tellus erat.",
                        "version": 2.53
                      },
                      {
                        "name": "Adil Eli",
                        "language": "Uyghur",
                        "id": "6VTI8X6LL0MMPJCC",
                        "bio": "Vivamus id faucibus velit, id posuere leo. Morbi vitae nisi lacinia, laoreet lorem nec, egestas orci. Suspendisse potenti.",
                        "version": 6.49
                      },
                      {
                        "name": "Adile Qadir",
                        "language": "Uyghur",
                        "id": "F2KEU5L7EHYSYFTT",
                        "bio": "Duis commodo orci ut dolor iaculis facilisis. Morbi ultricies consequat ligula posuere eleifend. Aenean finibus in tortor vel aliquet. Fusce eu ultrices elit, vel posuere neque.",
                        "version": 1.9
                      }
                    ]
                }
                """;
        System.out.println("before json " + Integer.toHexString(vertx.getOrCreateContext().hashCode()));
        final String val1 = vertx.getOrCreateContext().getLocal("key");

        final JsonObject jsonData = new JsonObject(data);

        System.out.println("after json " + Integer.toHexString(vertx.getOrCreateContext().hashCode()));
        final String val2 = vertx.getOrCreateContext().getLocal("key");
        assertEquals(val1, val2);
        testContext.completeNow();
    }

}
