import React from 'react';
import styles from './styles/home.module.css'
import PageFrame from '../components/common/PageFrame';
import Title from '../components/common/Title';
import SearchBar from '../components/searchBar/SearchBar';

function Home({ searchQuery, handleSearchInputChange, handleSearchResults }) {
  return (
    <PageFrame>
      <div className={styles.Home}>
        <Title fontSize={4.5}/>
        <SearchBar searchQuery={searchQuery} handleSearchInputChange={handleSearchInputChange} handleSearchResults={handleSearchResults}/>
      </div>
    </PageFrame>
  );
}

export default Home;
