package com.daw

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.lang.RuntimeException

class TestTable(
    var id: Int = 0,
    var name: String = ""
)

/**
 * To test, add Test table to Postgres with 2 columns: id and name
 * Insert 2 tuples and then run this test
 */
@SpringBootTest
class DatabaseTests {

    @Autowired
    lateinit var jdbi: Jdbi

    @Test
    fun can_access_db() {
        val projects = jdbi.withHandle<List<TestTable>, RuntimeException>{
            it.createQuery("select * from test")
                .mapToBean(TestTable::class.java)
                .list()
        }
        Assertions.assertEquals(2, projects.size)
    }
}