package com.min.redisson;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class DistributeAnnotationTest {

    @Autowired
    private PersonService personService;

    @Test
    @DisplayName("key 테스트")
    void test() {
        personService.test();
    }

    @Test
    @DisplayName("Lock 걸고 실행시 에러가 났을때 ExecuteContextException 이 아닌 해당 에러를 던져야한다.")
    void testException() {
        assertThatThrownBy(() -> personService.testWithNullPointerException("hello"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("매개변수가 key에 들어가야한다.")
    void testDynamicKey() {
        personService.testDynamicKey("hello");
    }

    @Test
    @DisplayName("매개변수의 메서드를 실행한 리턴값이 key에 들어가야한다.")
    void testDynamicKeyMethod() {
        personService.testDynamicKeyMethod("hello");
    }

    @Test
    @DisplayName("직접 만든 객체의 메서드를 실행한 리턴값이 key에 들어가야한다.")
    void testPersonDynamicKey() {
        personService.testDynamicKeyMethod(new Person("홍", "길동"));
    }

    @Test
    @DisplayName("두가지를 dynamicKey 를 이용해 키를 두개 만들어야 한다.")
    void testPersonTwoDynamicKey() {
        personService.testDynamicKeyMethod(new Person("홍", "길동"), "hello");
    }

}
