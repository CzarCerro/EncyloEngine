import React from 'react';
import PageFrame from '../components/common/PageFrame';
import Navbar from '../components/navBar/Navbar';
import styles from './styles/searchresult.module.css'
import SearchResultBlock from '../components/searchResultBlock/SearchResultBlock';
import NoResultBlock from '../components/searchResultBlock/NoResultBlock';

function SearchResult({ searchQuery, handleSearchInputChange, handleSearchResults, searchResults }) {
  return (
    <PageFrame>
      <Navbar 
        searchQuery={searchQuery} 
        handleSearchInputChange={handleSearchInputChange} 
        handleSearchResults={handleSearchResults}
      />
      <div className={styles.SearchResultBody}>
        {searchResults.length > 0 ? (
          searchResults.map((result, index) => (
            <SearchResultBlock
              key={index}
              url={result.url}
              title={result.title}
              content={result.content}
            />
          ))
        ) : (
          <NoResultBlock searchQuery={searchQuery}/>
        )}
      </div>
    </PageFrame>
  );
}

export default SearchResult;
