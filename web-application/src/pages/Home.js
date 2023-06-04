import React from 'react';

function Home({ searchQuery, handleSearchInputChange }) {
  
  const handleSearch = (e) => {
    e.preventDefault();
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

export default Home;
