package hr.ferit.blockchaindonations.repository;

import hr.ferit.blockchaindonations.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
}
