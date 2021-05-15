package com.github.xsi640.core

import org.springframework.data.jpa.repository.JpaRepository

interface BaseRepository<T, ID> : JpaRepository<T, ID>