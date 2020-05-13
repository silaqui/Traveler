package com.greenbee.traveler.domain.exceptions


sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()

    abstract class FeatureFailure: Failure()
}