package io.github.ititus.aoc.common;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static Map<AocDay, AocDaySolution> findSolutions(Package root) {
        String prefix = root.getName() + ".";
        ClassLoader cl = ReflectionUtil.class.getClassLoader();
        try {
            ClassPath cp = ClassPath.from(cl);
            return cp.getAllClasses().stream()
                    .filter(ci -> ci.getName().startsWith(prefix))
                    .map(ClassPath.ClassInfo::load)
                    .filter(AocDaySolution.class::isAssignableFrom)
                    .filter(c -> c.getDeclaredAnnotation(Aoc.class) != null)
                    .collect(Collectors.<Class<?>, AocDay, AocDaySolution>toMap(
                            c -> new AocDay(c.getDeclaredAnnotation(Aoc.class)),
                            c -> {
                                try {
                                    return (AocDaySolution) c.getConstructor().newInstance();
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            }));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
