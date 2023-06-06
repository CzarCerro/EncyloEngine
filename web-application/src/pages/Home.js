import React from 'react';
import SubmitButton from '../components/buttons/SubmitButton';
import styles from './styles/home.module.css'
import PageFrame from '../components/common/PageFrame';
import Title from '../components/common/Title';

function Home({ searchQuery, handleSearchInputChange }) {

  const handleSearch = (e) => {
    e.preventDefault();
  };

  return (
    <PageFrame>
      <div className={styles.Home}>
        <Title />
        <div class="input-group">
          <div class="form-outline">
            <input
              id="search-focus"
              type="search"
              class="form-control"
              onChange={handleSearchInputChange}
              placeholder="Enter your search query"
              style={{ width: '30rem' }}
              value={searchQuery}
            />
          </div>
          <SubmitButton />
        </div>


      </div>
    </PageFrame>
  );
}

export default Home;
