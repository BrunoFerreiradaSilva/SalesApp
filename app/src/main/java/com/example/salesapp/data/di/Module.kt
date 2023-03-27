package com.example.salesapp.data.di

import android.content.Context
import com.example.salesapp.data.database.OrderDAO
import com.example.salesapp.data.database.SalesDataBase
import com.example.salesapp.data.repository.SalesRepository
import com.example.salesapp.data.repository.SalesRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Provides
    fun providesSalesService(orderDAO: OrderDAO): SalesRepository {
        return SalesRepositoryImp(orderDAO)
    }

    @Provides
    fun providesOrderDAO(@ApplicationContext appContext: Context): OrderDAO {
        return SalesDataBase.getDatabase(appContext).orderDAO()
    }
}