import React, {useState} from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import SearchResult from './pages/SearchResult';

function App() {
  const [searchQuery, setSearchQuery] = useState('');

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
            />}
          />
          <Route
            path="/searchResult"
            element={<SearchResult
              handleSearchInputChange={handleSearchInputChange}
              searchQuery={searchQuery}
            />}
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
