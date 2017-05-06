package easycommand.core;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import easycommand.utils.CliUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CliUtilsTest {

    @Test
    public void test() {

        String[] parse = CliUtils.parse("cmd dir");

        log.info("{}", Arrays.toString(parse));
        Assert.assertEquals(2, parse.length);

        parse = CliUtils.parse("cmd \"dir c:/\"  ");

        log.info("{}", Arrays.toString(parse));
        Assert.assertEquals(2, parse.length);

        parse = CliUtils.parse("cmd \"dir c:/\"  \"dir d:/\"  ");

        log.info("{}", Arrays.toString(parse));
        Assert.assertEquals(3, parse.length);

    }

}
