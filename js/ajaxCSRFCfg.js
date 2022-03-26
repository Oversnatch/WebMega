/**
 * 
 */
$.ajaxSetup({headers:{'X-CSRF-TOKEN': $('META[NAME="_csrf"]').attr('content')}});