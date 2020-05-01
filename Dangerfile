# Ignore inline messages which lay outside a diff's range of PR
github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
if github.pr_json["requested_reviewers"].length != 0 && if github.pr_title.include? "[WIP]"
  warn("レビューのリクエストはWIPを外してからしましょう。") 
end

# make sure that where branch comes from
is_to_master = github.branch_for_base == 'master'
if is_to_master
  failure "Branchのベースがdevelop_ph2/*.*.*か確認しましょう。"
end
is_from_develop = github.branch_for_base.match(/develop_ph2/[0-9]+\.[0-9]+\.[0-9]/)
if !is_from_develop
  failure "Branchのベースがdevelop_ph2/*.*.*か確認しましょう。"
end

# note when a pr's changes is extremely big 
is_big_pr = git.lines_of_code > 500
if is_big_pr
  warn('PRの変更量が多いので、可能であればPRを分割しましょう。')
end

# ensure there is summary
if github.pr_body.length < 5
  warn "プルリクの内容を記載しましょう。"
end

# note when prs dont reference a milestone
has_milestone = github.pr_json["milestone"] != nil
warn("マイルストーンを設定しましょう。", sticky: false) unless has_milestone

# note when a pr cannot be merged
can_merge = github.pr_json["mergeable"]
warn("このPRはまだマージできません。", sticky: false) unless can_merge

# ktlint
# checkstyle_format.base_path = Dir.pwd
# checkstyle_format.report 'app/build/reports/ktlint/ktlint-main.xml'