# Ignore inline messages which lay outside a diff's range of PR
github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# branch
# if vsts.branch_for_base != "develop"
# failure "Please re-submit this PR to develop, we may have already fixed your issue."

# note when a pr's changes is extremely big 
is_big_pr = git.lines_of_code > 5
if is_big_pr
  warn('PRの変更量が多いので、可能であればPRを分割しましょう。')
end

# ensure there is summary
if github.pr_body.length < 5
  warn "Please provide a summary in the Pull Request description"
end

# note when prs dont reference a milestone
has_milestone = github.pr_json["milestone"] != nil
warn("This PR does not refer to an existing milestone", sticky: false) unless has_milestone

# note when a pr cannot be merged
can_merge = github.pr_json["mergeable"]
warn("This PR cannot be merged yet.", sticky: false) unless can_merge

# ktlint
# checkstyle_format.base_path = Dir.pwd
# checkstyle_format.report 'app/build/reports/ktlint/ktlint-main.xml'