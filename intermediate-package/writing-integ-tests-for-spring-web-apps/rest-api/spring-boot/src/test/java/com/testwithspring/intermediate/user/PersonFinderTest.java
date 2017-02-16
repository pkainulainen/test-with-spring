package com.testwithspring.intermediate.user;

import com.testwithspring.intermediate.UnitTest;
import com.testwithspring.intermediate.common.NotFoundException;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Optional;

import static com.testwithspring.intermediate.TestDoubles.stub;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(HierarchicalContextRunner.class)
@Category(UnitTest.class)
public class PersonFinderTest {

    private PersonFinder finder;
    private UserRepository repository;

    @Before
    public void configureSystemUnderTest() {
        repository = stub(UserRepository.class);
        finder = new PersonFinder(repository);
    }

    public class FindPersonInformationByUserId {

        private final Long USER_ID = 44L;

        public class WhenPersonInformationIsNotFound {

            @Before
            public void returnEmptyOptional() {
                given(repository.findPersonInformationById(USER_ID))
                        .willReturn(Optional.empty());
            }

            @Test(expected = NotFoundException.class)
            public void shouldThrowException() {
                finder.findPersonInformationByUserId(USER_ID);
            }
        }

        public class WhenPersonInformationIsFound {

            private final String NAME = "John Doe";

            @Before
            public void returnFoundPersonInformation() {
                PersonDTO found = new PersonDTO();
                found.setName(NAME);
                found.setUserId(USER_ID);

                given(repository.findPersonInformationById(USER_ID))
                        .willReturn(Optional.of(found));
            }

            @Test
            public void shouldReturnFoundPersonInformation() {
                PersonDTO found = finder.findPersonInformationByUserId(USER_ID);

                assertThat(found.getName()).isEqualTo(NAME);
                assertThat(found.getUserId()).isEqualByComparingTo(USER_ID);
            }
        }
    }
}
