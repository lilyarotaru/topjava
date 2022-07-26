package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Factory for creating test matchers.
 * <p>
 * Comparing actual and expected objects via AssertJ
 * Support converting json MvcResult to objects for comparation.
 */
public class MatcherFactory {
    public static <T> Matcher<T> usingIgnoringFieldsComparator(Class<T> clazz, String... fieldsToIgnore) {
        return new MatcherByFields<>(clazz, fieldsToIgnore);
    }

    public static <T> Matcher<T> usingEquals(Class<T> clazz) {
        return new MatcherByEquals<>(clazz);
    }

    public interface Matcher<T> {

        void assertMatch(T actual, T expected);

        void assertMatch(Iterable<T> actual, Iterable<T> expected);

        Class<T> getClazz();

        default void assertMatch(Iterable<T> actual, T... expected) {
            assertMatch(actual, List.of(expected));
        }

        default ResultMatcher contentJson(T expected) {
            return result -> assertMatch(JsonUtil.readValue(getContent(result), getClazz()), expected);
        }

        default ResultMatcher contentJson(T... expected) {
            return contentJson(List.of(expected));
        }

        default ResultMatcher contentJson(Iterable<T> expected) {
            return result -> assertMatch(JsonUtil.readValues(getContent(result), getClazz()), expected);
        }

        default T readFromJson(ResultActions action) throws UnsupportedEncodingException {
            return JsonUtil.readValue(getContent(action.andReturn()), getClazz());
        }

        private static String getContent(MvcResult result) throws UnsupportedEncodingException {
            return result.getResponse().getContentAsString();
        }
    }

    public static class MatcherByEquals<T> implements Matcher<T> {
        private final Class<T> clazz;

        private MatcherByEquals(Class<T> clazz) {
            this.clazz = clazz;
        }

        public void assertMatch(T actual, T expected) {
            assertThat(actual).isEqualTo(expected);
        }

        @SafeVarargs
        public final void assertMatch(Iterable<T> actual, T... expected) {
            assertMatch(actual, List.of(expected));
        }

        public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
            assertThat(actual).isEqualTo(expected);
        }

        @Override
        public Class<T> getClazz() {
            return clazz;
        }
    }

    public static class MatcherByFields<T> implements Matcher<T> {
        private final Class<T> clazz;
        private final String[] fieldsToIgnore;

        private MatcherByFields(Class<T> clazz, String... fieldsToIgnore) {
            this.clazz = clazz;
            this.fieldsToIgnore = fieldsToIgnore;
        }

        @Override
        public void assertMatch(T actual, T expected) {
            assertThat(actual).usingRecursiveComparison().ignoringFields(fieldsToIgnore).isEqualTo(expected);
        }

        @Override
        public void assertMatch(Iterable<T> actual, Iterable<T> expected) {
            assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields(fieldsToIgnore).isEqualTo(expected);
        }

        @Override
        public Class<T> getClazz() {
            return clazz;
        }
    }
}
