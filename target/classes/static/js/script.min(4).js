(function(){"use strict";if(!!window.JCSaleProductsGiftSectionComponent)return;window.JCSaleProductsGiftSectionComponent=function(e){this.formPosting=false;this.siteId=e.siteId||"";this.template=e.template||"";this.componentPath=e.componentPath||"";this.parameters=e.parameters||"";this.container=document.querySelector('[data-entity="'+e.container+'"]');if(e.initiallyShowHeader){BX.ready(BX.delegate(this.showHeader,this))}if(e.deferredLoad){BX.ready(BX.delegate(this.deferredLoad,this))}};window.JCSaleProductsGiftSectionComponent.prototype={deferredLoad:function(){this.sendRequest({action:"deferredLoad"})},sendRequest:function(e){var t={siteId:this.siteId,template:this.template,parameters:this.parameters};BX.ajax({url:this.componentPath+"/ajax.php"+(document.location.href.indexOf("clear_cache=Y")!==-1?"?clear_cache=Y":""),method:"POST",dataType:"json",timeout:60,data:BX.merge(t,e),onsuccess:BX.delegate(function(t){if(!t||!t.JS)return;BX.ajax.processScripts(BX.processHTML(t.JS).SCRIPT,false,BX.delegate(function(){this.showAction(t,e)},this))},this)})},showAction:function(e,t){if(!t)return;switch(t.action){case"deferredLoad":this.processDeferredLoadAction(e,t.bigData==="Y");break}},processDeferredLoadAction:function(e,t){if(!e)return;var i=t?this.bigData.rows:{};this.processItems(e.items,BX.util.array_keys(i))},processItems:function(e,t){if(!e)return;var i=BX.processHTML(e,false),a=BX.create("DIV");var s,n,o;a.innerHTML=i.HTML;s=a.querySelectorAll('[data-entity="items-row"]');if(s.length){this.showHeader(true);for(n in s){if(s.hasOwnProperty(n)){o=t?this.container.querySelectorAll('[data-entity="items-row"]'):false;s[n].style.opacity=0;if(o&&BX.type.isDomNode(o[t[n]])){o[t[n]].parentNode.insertBefore(s[n],o[t[n]])}else{this.container.appendChild(s[n])}}}new BX.easing({duration:2e3,start:{opacity:0},finish:{opacity:100},transition:BX.easing.makeEaseOut(BX.easing.transitions.quad),step:function(e){for(var t in s){if(s.hasOwnProperty(t)){s[t].style.opacity=e.opacity/100}}},complete:function(){for(var e in s){if(s.hasOwnProperty(e)){s[e].removeAttribute("style")}}}}).animate()}BX.ajax.processScripts(i.SCRIPT)},showHeader:function(e){var t=BX.findParent(this.container,{attr:{"data-entity":"parent-container"}}),i;if(t&&BX.type.isDomNode(t)){i=t.querySelector('[data-entity="header"]');if(i&&i.getAttribute("data-showed")!="true"){i.style.display="";if(e){new BX.easing({duration:2e3,start:{opacity:0},finish:{opacity:100},transition:BX.easing.makeEaseOut(BX.easing.transitions.quad),step:function(e){i.style.opacity=e.opacity/100},complete:function(){i.removeAttribute("style");i.setAttribute("data-showed","true")}}).animate()}else{i.style.opacity=100}}}}}})();
//# sourceMappingURL=script.map.js