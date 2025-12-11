// import logo from './logo.svg';
// import './App.css';
import {BrowserRouter,Routes,Route} from "react-router-dom";
import MainPortal from './main_portal';
import EightQueensGame from './components/EightQueensGame';

function App() {
  return(
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<MainPortal />}/>
          <Route path="/eight-queens" element={<EightQueensGame />}/>
        </Routes>
      </BrowserRouter>
  )
}

export default App;
