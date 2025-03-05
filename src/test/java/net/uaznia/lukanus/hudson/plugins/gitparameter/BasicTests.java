package net.uaznia.lukanus.hudson.plugins.gitparameter;

import static net.uaznia.lukanus.hudson.plugins.gitparameter.Constants.DEFAULT_VALUE;
import static net.uaznia.lukanus.hudson.plugins.gitparameter.Constants.NAME;
import static net.uaznia.lukanus.hudson.plugins.gitparameter.Constants.PT_REVISION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.kohsuke.stapler.StaplerRequest2;

/**
 * This test don't use jenkins rule
 */
class BasicTests {

    /**
     * Test of createValue method, of class GitParameterDefinition.
     */
    @Test
    void testCreateValue_StaplerRequest2() {
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                PT_REVISION,
                DEFAULT_VALUE,
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);

        StaplerRequest2 request = mock(StaplerRequest2.class);
        ParameterValue result = instance.createValue(request);

        assertEquals(new GitParameterValue(NAME, DEFAULT_VALUE), result);
    }

    @Test
    void matchesWithBitbucketPullRequestRefs() {
        Matcher matcher = Consts.PULL_REQUEST_REFS_PATTERN.matcher("refs/pull-requests/186/from");
        assertTrue(matcher.find());
        assertEquals("186", matcher.group(1));
    }

    @Test
    void matchesWithGithubPullRequestRefs() {
        Matcher matcher = Consts.PULL_REQUEST_REFS_PATTERN.matcher("refs/pull/45/head");
        assertTrue(matcher.find());
        assertEquals("45", matcher.group(1));
    }

    @Test
    void testCreateValue_StaplerRequest2_ValueInRequest() {
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                PT_REVISION,
                DEFAULT_VALUE,
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);

        StaplerRequest2 request = mock(StaplerRequest2.class);
        when(request.getParameterValues(instance.getName())).thenReturn(new String[] {"master"});
        ParameterValue result = instance.createValue(request);

        assertEquals(new GitParameterValue(NAME, "master"), result);
    }

    @Test
    void testConstructorInitializesTagFilterToAsteriskWhenNull() {
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                PT_REVISION,
                DEFAULT_VALUE,
                "description",
                "branch",
                null,
                null,
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);
        assertEquals("*", instance.getTagFilter());
        assertEquals(".*", instance.getBranchFilter());
    }

    @Test
    void testConstructorInitializesTagFilterToAsteriskWhenWhitespace() {
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                PT_REVISION,
                DEFAULT_VALUE,
                "description",
                "branch",
                "  ",
                "  ",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);
        assertEquals("*", instance.getTagFilter());
        assertEquals(".*", instance.getBranchFilter());
    }

    @Test
    void testConstructorInitializesTagFilterToAsteriskWhenEmpty() {
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                PT_REVISION,
                DEFAULT_VALUE,
                "description",
                "branch",
                "",
                "",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);
        assertEquals("*", instance.getTagFilter());
        assertEquals(".*", instance.getBranchFilter());
    }

    @Test
    void testConstructorInitializesTagToGivenValueWhenNotNullOrWhitespace() {
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                PT_REVISION,
                DEFAULT_VALUE,
                "description",
                "branch",
                "foobar",
                "foobar",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);
        assertEquals("foobar", instance.getTagFilter());
        assertEquals("foobar", instance.getBranchFilter());
    }

    /**
     * Test of createValue method, of class GitParameterDefinition.
     */
    @Test
    void testCreateValue_StaplerRequest2_JSONObject() {
        System.out.println("createValue");
        StaplerRequest2 request = mock(StaplerRequest2.class);

        Map<String, String> jsonR = new HashMap<>();
        jsonR.put("value", "Git_param_value");
        jsonR.put(NAME, "Git_param_name");

        JSONObject jO = JSONObject.fromObject(jsonR);

        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                PT_REVISION,
                DEFAULT_VALUE,
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);

        ParameterValue result = instance.createValue(request, jO);

        assertEquals(new GitParameterValue("Git_param_name", "Git_param_value"), result);
    }

    /**
     * Test of getType method, of class GitParameterDefinition.
     */
    @Test
    void testGetParameterType() {
        String expResult = PT_REVISION;
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                expResult,
                DEFAULT_VALUE,
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);
        String result = instance.getParameterType();
        assertEquals(expResult, result);

        instance.setParameterType(expResult);
        result = instance.getParameterType();
        assertEquals(expResult, result);
    }

    /**
     * Test of setType method, of class GitParameterDefinition.
     */
    @Test
    void testSetParameterType() {
        String expResult = PT_REVISION;
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                "asdf",
                DEFAULT_VALUE,
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);

        instance.setParameterType(expResult);
        String result = instance.getParameterType();
        assertEquals(expResult, result);
    }

    @Test
    void testWrongType() {
        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                "asdf",
                DEFAULT_VALUE,
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);

        String result = instance.getParameterType();
        assertEquals("PT_BRANCH", result);
    }

    /**
     * Test of getDefaultValue method, of class GitParameterDefinition.
     */
    @Test
    void testGetDefaultValue() {
        System.out.println("getDefaultValue");
        String expResult = DEFAULT_VALUE;

        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                "asdf",
                expResult,
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);
        String result = instance.getDefaultValue();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDefaultValue method, of class GitParameterDefinition.
     */
    @Test
    void testSetDefaultValue() {
        System.out.println("getDefaultValue");
        String expResult = DEFAULT_VALUE;

        GitParameterDefinition instance = new GitParameterDefinition(
                NAME,
                "asdf",
                "other",
                "description",
                "branch",
                ".*",
                "*",
                SortMode.NONE,
                SelectedValue.NONE,
                null,
                false);
        instance.setDefaultValue(expResult);

        String result = instance.getDefaultValue();
        assertEquals(expResult, result);
    }
}
