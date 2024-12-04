package by.course.govservices.controller

import by.course.govservices.dto.DocumentDto
import by.course.govservices.service.DocumentService
import by.course.govservices.util.FilterCriteria
import by.course.govservices.util.PaginationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/documents")
class DocumentController(private val documentService: DocumentService) {

    // Получение документа по ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getDocumentById(@PathVariable id: Long): DocumentDto {
        return documentService.findById(id)
    }

    // Получение всех документов с фильтрами и пагинацией
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllDocuments(
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @RequestParam(required = false) link: String? = null,
        @RequestParam(required = false) typeId: Int? = null,
        @RequestParam(required = false) loadingDate: String? = null,  // Формат: yyyy-MM-dd
        @RequestParam(required = false) bidId: Int? = null
    ): List<DocumentDto> {
        val filters = mutableListOf<FilterCriteria>()
        link?.let { filters.add(FilterCriteria("link", it)) }
        typeId?.let { filters.add(FilterCriteria("typeId", it.toString())) }
        loadingDate?.let { filters.add(FilterCriteria("loadingDate", it)) }
        bidId?.let { filters.add(FilterCriteria("bidId", it.toString())) }

        val pagination = PaginationRequest(page, size)
        return documentService.findAll(pagination, filters).content  // Возвращаем только содержимое страницы
    }

    // Создание нового документа
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createDocument(@RequestBody documentDto: DocumentDto): DocumentDto {
        return documentService.save(documentDto)
    }

    // Обновление существующего документа
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateDocument(@PathVariable id: Long, @RequestBody documentDto: DocumentDto): DocumentDto {
        return documentService.update(id, documentDto)
    }

    // Удаление документа
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDocument(@PathVariable id: Long) {
        documentService.delete(id)
    }
}
