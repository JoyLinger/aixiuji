function createStore(initialState){
  var state = initialState || {};
  var list = [];
  // 取值
  function getState(type){
    return state[type];
  }
  // 修改
  function dispatch(action){
    state[action.type] = action.value;
    list.foreach(function(ele){
      ele()
    })
  }

  function subscribe(func){
    list.push(func)
  }
  // 返回函数, 外界可通过store.getState()等函数进行取值/修改等操作
  return {
    getState: getState,
    dispatch: dispatch,
    subscribe: subscribe
  }
}
// 初始化全局默认值
var store = createStore({text: '', sex: 'a'});