<template>
  <!-- template div는 하나만 있어야함 (root 니깐) -->
  <div id="app">
    <!-- <app-header v-vind:프롭스 속성 이름="상위 컴포넌트의 데이터 이름"></app-header> -->
    <app-header 
      v-bind:propsdata="str"
      v-on:renew="renewStr">
     </app-header>
  </div>

</template>

<script>
import AppHeader from './components/AppHeader.vue'
// new Vue({
//   data: {
//     str: 'hi'
//   }
// })

export default {
  // 아래는 ... 이제 여기서만 쓸 객체를 리턴해주느 걸로
  data: function() {
    return {
      str: 'Header'
    }
  },
  components: {
    'app-header': AppHeader
  },
  methods: {
    renewStr: function() {
      this.str = 'hi';
    }
  }
}
</script>

<style>

</style>