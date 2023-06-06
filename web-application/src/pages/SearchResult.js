import React from 'react';
import PageFrame from '../components/common/PageFrame';
import Title from '../components/common/Title';
import SubmitButton from '../components/buttons/SubmitButton';

function SearchResult({ searchQuery, handleSearchInputChange }) {

  const handleSearch = (e) => {
    e.preventDefault();
    // Perform search logic with the searchQuery value
    console.log('Performing search for:', searchQuery);
  };

  return (
    <PageFrame>
      <div>
        <Title />
        <div class="input-group">
          <div class="form-outline">
            <input
              id="search-focus"
              type="search"
              class="form-control"
              onChange={handleSearchInputChange}
              placeholder="Enter your search query"
              value={searchQuery}
            />
          </div>
          <SubmitButton />
        </div>
        {/* Display search results below */}
      </div>
    </PageFrame>
  );
}

export default SearchResult;
