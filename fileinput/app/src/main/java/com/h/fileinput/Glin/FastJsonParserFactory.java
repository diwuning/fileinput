package com.h.fileinput.Glin;

import org.loader.glin.factory.ParserFactory;
import org.loader.glin.parser.Parser;

/**
 * Created by pc on 2018/6/4.
 */

public class FastJsonParserFactory implements ParserFactory {
    @Override
    public Parser getParser() {
        return new CommParser(null);
    }

    @Override
    public Parser getListParser() {
        return new ListParser("books");
    }
}
