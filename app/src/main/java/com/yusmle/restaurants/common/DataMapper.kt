package com.yusmle.restaurants.common

abstract class DataMapper<M, E> {

    abstract fun transformToEntity(model: M): E

    abstract fun transformToModel(entity: E): M

    fun transformToEntities(models: List<M>): List<E> = models.map {
        transformToEntity(it)
    }

    fun transformToModels(entities: List<E>): List<M> = entities.map {
        transformToModel(it)
    }
}
