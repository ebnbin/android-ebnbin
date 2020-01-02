package com.ebnbin.eb.library

import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson = GsonBuilder()
    .setPrettyPrinting()
    .create()
