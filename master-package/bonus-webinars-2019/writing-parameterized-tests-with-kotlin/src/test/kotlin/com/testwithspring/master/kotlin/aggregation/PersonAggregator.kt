package com.testwithspring.master.kotlin.aggregation

import com.testspringmaster.kotlin.example.Person
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.aggregator.ArgumentsAggregator
import java.lang.IllegalArgumentException

class PersonAggregator: ArgumentsAggregator {

    override fun aggregateArguments(arguments: ArgumentsAccessor, context: ParameterContext): Any {
        if (arguments.size() != 2) throw IllegalArgumentException("This arguments aggregator requires exactly two arguments")
        return Person(arguments.getLong(0), arguments.getString(1))
    }
}