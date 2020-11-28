package com.wistronics.sqlfmt.utils

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import StringUtils._

class StringUtilSpec extends AnyFlatSpec with Matchers {

  "StringUtil.toUnderscore" should "convert camel-case to underscore convention" in {
    "experienceEvent01".toUnderscore shouldBe "experience_event_01"
    "experience_event".toUnderscore shouldBe "experience_event"
    "experienceEventType".toUnderscore shouldBe "experience_event_type"
    "experience Event Type".toUnderscore shouldBe "experience_event_type"
    "experience EVENT Type".toUnderscore shouldBe "experience_event_type"
    "experience-EVENT-Type".toUnderscore shouldBe "experience_event_type"
    "experience/EVENT-Type".toUnderscore shouldBe "experience_event_type"
    "userId".toUnderscore shouldBe "user_id"
    "John La. Smith".toUnderscore shouldBe "john_la._smith"
    "John L. Smith".toUnderscore shouldBe "john_l._smith"
    "experienceEventType".toUnderscore shouldBe "experience_event_type"
    "isPaid".toUnderscore shouldBe "is_paid"
    "is800X600ScreenResolution".toUnderscore shouldBe "is_800_x_600_screen_resolution"
    "propertiesDateOfBirthValue".toUnderscore shouldBe "properties_date_of_birth_value"
    "propertiesDateofbirthValue".toUnderscore shouldBe "properties_dateofbirth_value"
    "qualificationTier__c".toUnderscore shouldBe "qualification_tier__c"
  }

  "StringUtil.fromUnderscore" should "convert underscore to camel-case convention" in {
    "experience_event_0_1".fromUnderscore shouldBe "experienceEvent01"
    "experience_event_01".fromUnderscore shouldBe "experienceEvent01"
    "experience_event".fromUnderscore shouldBe "experienceEvent"
    "experience_event_type".fromUnderscore shouldBe "experienceEventType"
    "user_id".fromUnderscore shouldBe "userId"
    "john_l._smith".fromUnderscore shouldBe "johnL.Smith"
    "john_la._smith".fromUnderscore shouldBe "johnLa.Smith"
    "experience_event_type".fromUnderscore shouldBe "experienceEventType"
    "is_paid".fromUnderscore shouldBe "isPaid"
    "is_800_x_600_screen_resolution".fromUnderscore shouldBe "is800X600ScreenResolution"
    "properties_date_of_birth_value".fromUnderscore shouldBe "propertiesDateOfBirthValue"
    "properties_dateofbirth_value".fromUnderscore shouldBe "propertiesDateofbirthValue"
  }

  "StringUtil.sanitize" should "replace all non printable chars and double spaces with a single space" in {
    "  abcd.  efgh  ".sanitize shouldBe "abcd. efgh"
  }

  "StringUtil.findAllBetween" should "find all substrings between prefix and sufix" in {
    "a (1)(2)a( 3 ) b".substringsBetween("(", Some(")")) shouldBe Seq("1", "2", " 3 ")
  }
  it should "find all substrings between chars" in {
    "a |1||2|a| 3 | b".substringsBetween("|") shouldBe Seq("1", "2", " 3 ")
  }
}
