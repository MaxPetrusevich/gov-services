package by.course.govservices.service

import by.course.govservices.dto.TypeDto
import by.course.govservices.entities.Type
import by.course.govservices.exceptions.DataFormatException
import by.course.govservices.exceptions.NotFoundException
import by.course.govservices.repositories.TypeRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TypeService(
    private val typeRepository: TypeRepository
) {

    // Поиск типа по коду
    fun findByCode(code: String): Mono<TypeDto> {
        return typeRepository.findByType(code)
            .map { it.toDto() }  // Преобразуем Type в TypeDto
            .switchIfEmpty(Mono.error(NotFoundException("Type with code $code not found")))
    }

    // Преобразование Type в TypeDto
    private fun Type.toDto(): TypeDto = TypeDto(id, type)

    // Преобразование TypeDto в сущность Type
    fun save(typeDto: TypeDto): Mono<TypeDto> {
        val type = Type(typeDto.id, typeDto.type)
        return typeRepository.save(type)
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to save type: ${e.message}") }
    }

    // Обновление типа
    fun update(id: Long, typeDto: TypeDto): Mono<TypeDto> {
        return typeRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Type with ID $id not found")))
            .flatMap { typeRepository.save(typeDto.toEntity()) }
            .map { it.toDto() }
            .onErrorMap { e -> DataFormatException("Failed to update type: ${e.message}") }
    }

    // Удаление типа
    fun delete(id: Long): Mono<Void> {
        return typeRepository.findById(id)
            .switchIfEmpty(Mono.error(NotFoundException("Type with ID $id not found")))
            .flatMap { typeRepository.delete(it) }
            .onErrorMap { e -> DataFormatException("Failed to delete type: ${e.message}") }
    }

    // Преобразование TypeDto в сущность Type
    private fun TypeDto.toEntity(): Type = Type(id, type)
}
