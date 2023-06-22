import React, {useState} from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import SearchResult from './pages/SearchResult';
import 'bootstrap/dist/css/bootstrap.css';

function App() {
  const [searchQuery, setSearchQuery] = useState('');
  const [searchResults, setSearchResults] = useState([]);

  const handleSearchResults = (results) => {
    setSearchResults(results);
  };

  const handleSearchInputChange = (event) => {
    console.log(searchQuery)
    setSearchQuery(event.target.value);
  };

  return (
    <Router>
      <div className="App">
        <Routes>
          <Route
            path="/"
            element={<Home
              handleSearchInputChange={handleSearchInputChange}
              searchQuery={searchQuery}
              handleSearchResults={handleSearchResults}
            />}
          />
          <Route
            path="/searchResult"
            element={<SearchResult
              handleSearchInputChange={handleSearchInputChange}
              searchQuery={searchQuery}
              handleSearchResults={handleSearchResults}
              searchResults={searchResults}
            />}
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
