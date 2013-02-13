//
//  VideoTableItemCell.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "VideoTableItemCell.h"
#import "Video.h"
#import "VideoImageView.h"

@implementation VideoTableItemCell

@synthesize videoTableItem = _videoTableItem;
@synthesize titleArray = _titleArray;
@synthesize imageArray = _imageArray;

+ (CGFloat)tableView:(UITableView*)tableView rowHeightForObject:(id)object {
    return 130;
}


- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString*)identifier {
    
    self = [super initWithStyle:style reuseIdentifier:identifier];
    if (self) {
        self.titleArray = [[NSMutableArray array] retain];
        self.imageArray = [[NSMutableArray array] retain];
        for(int i=0;i<3;i++){
            VideoImageView* imageView = [[[VideoImageView alloc] autorelease] initWithFrame:CGRectZero];
            TTStyledTextLabel* titleLabel = [[[TTStyledTextLabel alloc] autorelease] init];
            [self.titleArray addObject:titleLabel];
            [self.imageArray addObject:imageView];
            [self.contentView addSubview:imageView];
            [self.contentView addSubview:titleLabel];
        }
        
    }
    
	return self;
}

- (void)dealloc {
    TT_RELEASE_SAFELY(_titleArray);
    TT_RELEASE_SAFELY(_imageArray);
	[super dealloc];
}
///////////////////////////////////////////////////////////////////////////////////////////////////
// UIView

- (void)layoutSubviews {
	[super layoutSubviews];
    for(int i=0;i<3;i++){
        VideoImageView* imageView = [self.imageArray objectAtIndex:i];
        TTStyledTextLabel* titleLabel = [self.titleArray objectAtIndex:i];
        imageView.frame = CGRectMake(5+77*i, 10, 75, 100);
        imageView.layer.cornerRadius = 8;
        imageView.layer.masksToBounds = YES;
        imageView.actionUrl=@"atvserver://qq/video/%@";
        titleLabel.frame = CGRectMake(5+77*i, 110, 75, 10);
    }
}


///////////////////////////////////////////////////////////////////////////////////////////////////
// TTTableViewCell


- (void)setObject:(id)object {
    self.videoTableItem = object;
    for(int i=0;i<3;i++){
        TTImageView* imageView = [self.imageArray objectAtIndex:i];
        Video* video = [self.videoTableItem.videoList objectAtIndex:i];
        imageView.urlPath = [video picUrl];
    }
    [super setObject:object];
}


///////////////////////////////////////////////////////////////////////////////////////////////////
- (id)object {
    return self.videoTableItem;
}

@end
