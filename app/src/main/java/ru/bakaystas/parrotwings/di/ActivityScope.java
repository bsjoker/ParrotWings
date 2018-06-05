package ru.bakaystas.parrotwings.di;

/**
 * Created by 1 on 20.04.2018.
 */

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope{

}
