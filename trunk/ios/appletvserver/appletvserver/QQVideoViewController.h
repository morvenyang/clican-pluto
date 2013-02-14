//
//  QQVideoViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "Video.h"
#import "QQVideoRequestModel.h"


@interface QQVideoViewController : TTViewController<QQVideoDelegate,TTImageViewDelegate>{
    Video* _video;
    QQVideoRequestModel* _qqVideoRequestModel;
    TTStyledTextLabel* _summaryTextLabel;
    TTStyledTextLabel* _descriptionTextLabel;
    UIButton* _playButton;
    TTImageView* _imageView;
    UIView* _reflectImageView;
}

@property (nonatomic, retain) Video* video;
@property (nonatomic, retain) QQVideoRequestModel* qqVideoRequestModel;
@property (nonatomic, retain) TTStyledTextLabel* summaryTextLabel;
@property (nonatomic, retain) TTStyledTextLabel* descriptionTextLabel;
@property (nonatomic, retain) UIButton* playButton;
@property (nonatomic, retain) TTImageView* imageView;
@property (nonatomic, retain) UIView* reflectImageView;

- (id) initWithVid:(NSNumber*) vid;

@end
