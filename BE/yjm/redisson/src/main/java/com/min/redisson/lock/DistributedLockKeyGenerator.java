package com.min.redisson.lock;

import lombok.experimental.UtilityClass;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class DistributedLockKeyGenerator {
    private static final String DELIMITER = ":";

    public static List<String> generateKeys(final String keyPrefix,
                                            final String[] spelExpressions,
                                            final String[] parameterNames,
                                            final Object[] args) throws DistributedLockKeyGenerateException {
        if (spelExpressions.length == 0) {
            return Collections.singletonList(keyPrefix);
        }

        final List<String> keys = Arrays.stream(spelExpressions)
                .map(spelExpression -> generateKey(keyPrefix, spelExpression, parameterNames, args))
                .toList();

        if (keys.size() == 0) {
            throw new DistributedLockKeyGenerateException();
        }

        return keys;
    }

    public static String generateKey(final String keyPrefix,
                                     final String spelExpression,
                                     final String[] parameterNames,
                                     final Object[] args) {
        final Object dynamicKey = DistributedLockKeyGenerator.parseExpression(spelExpression, parameterNames, args);
        return keyPrefix + DELIMITER + dynamicKey.toString();
    }

    private static Object parseExpression(final String spelExpression,
                                          final String[] parameterNames,
                                          final Object[] args) {
        final ExpressionParser parser = new SpelExpressionParser();
        final StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        return parser.parseExpression(spelExpression).getValue(context, Object.class);
    }

}