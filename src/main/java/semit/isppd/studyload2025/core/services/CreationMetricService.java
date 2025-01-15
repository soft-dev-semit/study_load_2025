package semit.isppd.studyload2025.core.services;

import semit.isppd.studyload2025.core.entities.CreationMetric;
import semit.isppd.studyload2025.core.repositories.CreationMetricRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CreationMetricService {

    private final CreationMetricRepository metricRepository;

    public CreationMetricService(CreationMetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    @Transactional
    public void save(CreationMetric creationMetric) {
        metricRepository.save(creationMetric);
    }
}
