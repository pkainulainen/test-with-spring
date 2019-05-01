package com.testwithspring.master.kotlin.conversion

import org.junit.jupiter.params.converter.SimpleArgumentConverter
import java.lang.IllegalArgumentException

/**
 * This is a custom argument converter that can transform
 * a {@code String} object into an {@code Album} object.
 */
class AlbumArgumentConverter: SimpleArgumentConverter() {

    override fun convert(source: Any?, targetType: Class<*>?): Any {
        if (targetType != Album::class.java) throw IllegalArgumentException("This converter can create only Album objects")

        return Album(source.toString())
    }
}