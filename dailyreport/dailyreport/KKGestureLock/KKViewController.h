//
//  KKViewController.h
//  KKGestureLockView
//
//  Created by Luke on 8/5/13.
//  Copyright (c) 2013 geeklu. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "KKGestureLockView.h"

@interface KKViewController : UIViewController<KKGestureLockViewDelegate>{
    NSString* _type;
    NSString* _password;
    NSString* _confirmedPassword;
    int _time;
    UILabel* _titleLabel;
}

@property (nonatomic, strong) KKGestureLockView *lockView;
@property (nonatomic, copy) NSString* type;
@property (nonatomic, copy) NSString* password;
@property (nonatomic, copy) NSString* confirmedPassword;
@property (nonatomic, assign) int time;
@property (nonatomic, strong) UILabel* titleLabel;
-(id) initWithType:(NSString*) type;
@end
