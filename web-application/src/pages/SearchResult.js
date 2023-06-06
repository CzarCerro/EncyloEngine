import React from 'react';
import PageFrame from '../components/common/PageFrame';
import Title from '../components/common/Title';
import SearchBar from '../components/searchBar/SearchBar';

function SearchResult({ searchQuery, handleSearchInputChange }) {

  return (
    <PageFrame>
      <div>
        <Title fontSize={2}/>
        <SearchBar searchQuery={searchQuery} handleSearchInputChange={handleSearchInputChange}/>
        {/* Display search results below */}
      </div>
    </PageFrame>
  );
}

export default SearchResult;
