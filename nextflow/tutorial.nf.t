#!/usr/bin/env nextflow

params.str = 'Hello world!'

process splitLetters {

//    queue 'long'
//    executor 'pbs'
    container = 'andreagia/hhsuite2:16.04'
    tag = '#REPLACETAG#'

    output:
    file 'chunk_*' into letters

    """
    printf '${params.str}' | split -b 6 - chunk_
    """
}


process convertToUpper {
    
    tag = '#REPLACETAG#'

    input:
    file x from letters.flatten()

    output:
    stdout result

    """
    sleep 15
    cat $x | tr '[a-z]' '[A-Z]'
    """
}

result.view { it.trim() }
