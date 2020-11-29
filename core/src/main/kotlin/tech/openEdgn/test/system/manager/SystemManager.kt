package tech.openEdgn.test.system.manager

import tech.openEdgn.test.system.memory.IMemoryAlgorithm
import tech.openEdgn.test.system.process.IProcessAlgorithm
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 *
 * 进程启动器
 *
 */
abstract class SystemManager(processImpl: KClass<out IProcessAlgorithm>, memoryImpl: KClass<out IMemoryAlgorithm>) : ISystemManager {



}