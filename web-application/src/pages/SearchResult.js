import React from 'react';

function SearchResult({ searchQuery, handleSearchInputChange }) {
  
  const handleSearch = (e) => {
    e.preventDefault();
    // Perform search logic with the searchQuery value
    console.log('Performing search for:', searchQuery);
  };

  return (
    <div>
      <h1>Search Engine</h1>
      <form onSubmit={handleSearch}>
        <input
          type="text"
          value={searchQuery}
          onChange={handleSearchInputChange}
          placeholder="Enter your search query"
        />
        <button type="submit">Search</button>
      </form>
      {/* Display search results below */}
    </div>
  );
}

export default SearchResult;
