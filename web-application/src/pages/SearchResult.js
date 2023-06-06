import React from 'react';
import PageFrame from '../components/common/PageFrame';
import Navbar from '../components/navBar/Navbar';
import styles from './styles/searchresult.module.css'
import SearchResultBlock from '../components/searchResultBlock/SearchResultBlock';
import mockData from '../mockData.js'


function SearchResult({ searchQuery, handleSearchInputChange }) {

  const data = mockData();

  return (
    <PageFrame>
      <Navbar searchQuery={searchQuery} handleSearchInputChange={handleSearchInputChange} />
      <div className={styles.SearchResultBody}>
        {
          Array.from({ length: 10 }, (_, index) => (
            <SearchResultBlock
              key={index}
              url={data.url}
              title={data.title}
              content={data.content}
            />
          ))
        }
      </div>
    </PageFrame>
  );
}

export default SearchResult;
