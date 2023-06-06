import React from 'react';
import PageFrame from '../components/common/PageFrame';
import Title from '../components/common/Title';
import SearchBar from '../components/searchBar/SearchBar';
import Navbar from '../components/navBar/Navbar';

function SearchResult({ searchQuery, handleSearchInputChange }) {

  return (
    <PageFrame>
      <Navbar searchQuery={searchQuery} handleSearchInputChange={handleSearchInputChange}/>
    </PageFrame>
  );
}

export default SearchResult;
