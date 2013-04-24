//
//  InputViewController.h
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import <Three20UI/Three20UI.h>
#import <JavaScriptCore/JavaScriptCore.h>

@interface InputViewController : TTTableViewController<UITextFieldDelegate>{
    NSString* _label;
    NSString* _instruction;
    NSString* _initialText;
    JSObjectRef _callback;
    JSContextRef _ctx;
    
    UITextField* _textField;
    UIButton* _submitButton;
}

@property (nonatomic, copy) NSString* label;
@property (nonatomic, copy) NSString* instruction;
@property (nonatomic, copy) NSString* initialText;
@property (nonatomic, assign) JSObjectRef callback;
@property (nonatomic, assign) JSContextRef ctx;
@property (nonatomic, retain) UITextField *textField;
@property (nonatomic, retain) UIButton *submitButton;

-(id) initWithLabel:(NSString*) label instruction:(NSString*)instruction initialText:(NSString*) initialText callback:(JSObjectRef) callback ctx:(JSContextRef) ctx;
@end
