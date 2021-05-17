package com.github.xsi640.core

fun String.wildcardMatch(pattern: String): Boolean {
    if (this.isEmpty() && pattern.isNullOrEmpty()) {
        return true
    }
    if (this.isNotEmpty() && pattern.isNullOrEmpty()) {
        return false
    }
    if (this.isNullOrEmpty()) {
        for (c in pattern.toCharArray()) {
            if (c != '*') {
                return false
            }
        }
        return true
    }
    return wildcardMatchingInternal(this, pattern, 0, 0)
}

private fun wildcardMatchingInternal(input: String, pattern: String, iIndex: Int, pIndex: Int): Boolean {
    if (iIndex >= input.length && pIndex >= pattern.length) {
        return true
    }
    if (iIndex >= input.length && pattern[pIndex] != '*') {
        return false
    }
    if (iIndex >= input.length && pattern[pIndex] == '*') {
        return wildcardMatchingInternal(input, pattern, iIndex, pIndex + 1)
    }
    if (iIndex < input.length && pIndex >= pattern.length) {
        return false
    }
    if (input[iIndex] == pattern[pIndex]) {
        return wildcardMatchingInternal(input, pattern, iIndex + 1, pIndex + 1)
    }
    if (pattern[pIndex] == '?') {
        return wildcardMatchingInternal(input, pattern, iIndex + 1, pIndex + 1)
    }
    return if (pattern[pIndex] == '*') {
        wildcardMatchingInternal(input, pattern, iIndex + 1, pIndex) ||
                wildcardMatchingInternal(input, pattern, iIndex, pIndex + 1)
    } else false
}