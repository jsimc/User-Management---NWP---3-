package rs.raf.demo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.model.User;
import rs.raf.demo.repositories.ErrorMessageRepository;

import java.util.List;
import java.util.Optional;
@Service
public class ErrorMessageService implements IService<ErrorMessage, Long> {
    private ErrorMessageRepository errorMessageRepository;

    public ErrorMessageService(ErrorMessageRepository errorMessageRepository) {
        this.errorMessageRepository = errorMessageRepository;
    }

    public Page<ErrorMessage> paginate(Integer page, Integer size) {
        return this.errorMessageRepository.findAll(PageRequest.of(page, size, Sort.by("dateCreated").descending()));
    }

    @Override
    public ErrorMessage save(ErrorMessage errorMessage) {
        return this.errorMessageRepository.save(errorMessage);
    }

    @Override
    public Optional<ErrorMessage> findById(Long id) {
        return this.errorMessageRepository.findById(id);
    }

    @Override
    public List<ErrorMessage> findAll() {
        return this.errorMessageRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        this.errorMessageRepository.deleteById(id);
    }
}
